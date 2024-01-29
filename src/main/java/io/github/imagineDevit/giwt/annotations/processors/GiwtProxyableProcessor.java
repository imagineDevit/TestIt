package io.github.imagineDevit.giwt.annotations.processors;


import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.imagineDevit.giwt.annotations.processors.Constants.*;

@SupportedAnnotationTypes("io.github.imagineDevit.giwt.annotations.GiwtProxyable")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class GiwtProxyableProcessor extends AbstractProcessor {


    VelocityEngine velocityEngine;
    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {

        super.init(processingEnv);

        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();

        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, CLASSPATH);
        velocityEngine.setProperty(CLASSPATH_RESOURCE_LOADER_CLASS, ClasspathResourceLoader.class.getName());
        velocityEngine.init();

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        annotations.forEach(annotation -> {

            for (Element element: roundEnv.getElementsAnnotatedWith(annotation)) {

                TypeElement typeElement = (TypeElement) element;

                List<MethodProcessingData> datas =typeElement.getEnclosedElements()
                        .stream()
                        .filter(e -> e instanceof ExecutableElement)
                        .map(e -> (ExecutableElement) e)
                        .filter(e -> !Objects.equals(e.getSimpleName().toString(), INIT) && e.getModifiers().contains(Modifier.PUBLIC))
                        .map(MethodProcessingData::from)
                        .collect(Collectors.toList());

                try {
                    writeFromTemplate(datas, typeElement);
                } catch (IOException e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                    throw new RuntimeException(e);
                }
            }

        });

        return true;
    }

    private void writeFromTemplate(List<MethodProcessingData> datas, TypeElement element) throws IOException {

        final Template template = velocityEngine.getTemplate(TEMPLATE_NAME);

        VelocityContext context = new VelocityContext();

        String simpleClassName = element.getSimpleName().toString();

        var generatedClassName = "%s%s".formatted(simpleClassName, TESTER);

        context.put(PACKAGE_NAME, elementUtils.getPackageOf(element).toString());
        context.put(CLASS_NAME, simpleClassName);
        context.put(GENERATED_CLASS_NAME, generatedClassName);
        context.put(METHOD_DATAS, datas);

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(generatedClassName);

        try(Writer writer = new PrintWriter(builderFile.openWriter())) {
            template.merge(context, writer);
        }
    }
}
