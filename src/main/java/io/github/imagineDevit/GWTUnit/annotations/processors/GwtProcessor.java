package io.github.imagineDevit.GWTUnit.annotations.processors;


import com.google.auto.service.AutoService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.github.imagineDevit.GWTUnit.annotations.processors.Constants.*;

@SupportedAnnotationTypes("io.github.imagineDevit.GWTUnit.annotations.Gwt")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class GwtProcessor extends AbstractProcessor {

    VelocityEngine velocityEngine;
    private Messager messager;
    private Types typeUtils;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {

        super.init(processingEnv);

        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();

        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        annotations.forEach(annotation -> {

            // get elements annotated @Gwt
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);


            for (Element elmt: annotatedElements) {

                List<MethodProcessingData> datas = new ArrayList<>();

                TypeElement typeElement = (TypeElement) elmt;

                for (Element e: typeElement.getEnclosedElements()) {

                    if (e instanceof ExecutableElement method) {

                        if (!Objects.equals(method.getSimpleName().toString(), INIT) && method.getModifiers().contains(javax.lang.model.element.Modifier.PUBLIC)) {
                           datas.add(MethodProcessingData.from(method));
                        }
                    }

                }

                try {
                    writeFromTemplate(datas, typeElement.getQualifiedName().toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        });

        return true;
    }

    private void writeFromTemplate(List<MethodProcessingData> datas, String className) throws IOException {

        final Template template = velocityEngine.getTemplate(TEMPLATE_NAME);

        VelocityContext context = new VelocityContext();

        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String simpleClassName = className.substring(lastDot + 1);

        var generatedClassName = "%s%s".formatted(simpleClassName, TESTER);

        context.put(PACKAGE_NAME, packageName);
        context.put(CLASS_NAME, simpleClassName);
        context.put(GENERATED_CLASS_NAME, generatedClassName);
        context.put(METHOD_DATAS, datas);

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(generatedClassName);

        try(Writer writer = new PrintWriter(builderFile.openWriter())) {
            template.merge(context, writer);
        }
    }
}
