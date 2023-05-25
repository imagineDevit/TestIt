<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe"
            crossorigin="anonymous"></script>
    <title>Title</title>
    <script>
        function showStackTrace(id) {
            document.getElementById(id).style.display = "block";
            document.getElementById("showStack").style.display = "none";
            document.getElementById("hideStack").style.display = "block"
        }

        function hideStacktrace(id) {
            document.getElementById(id).style.display = "none";
            document.getElementById("showStack").style.display = "block";
            document.getElementById("hideStack").style.display = "none"
        }
    </script>
</head>
<body>
<div class="container">
    <div class="mx-auto">
        <div class="container mt-3 p-3 bg-light w-75 border border-primary rounded">
            <h2 class="text-primary text-center "> TEST SUMMARY </h2>
        </div>

        <div class="container px-4 text-center w-75 p-3">
            <div class="row gx-5 justify-content-between align-items-center">
                <div class="col-7 bg-light rounded p-3">
                    <div class="row">
                        <div class="col">
                            <h3>
                                ${report.totalCount}
                            </h3>
                            <p class="fs-1fst-italic font-monospace">tests</p>
                        </div>
                        <div class="col text-danger">
                            <h3>
                                ${report.failureCount}
                            </h3>
                            <p class="fs-1fst-italic font-monospace">failures</p>
                        </div>
                        <div class="col text-warning">
                            <h3>
                                ${report.skippedCount}
                            </h3>
                            <p class="fs-1fst-italic font-monospace">skipped</p>
                        </div>
                    </div>
                </div>
                <div class="col-4 bg-success rounded p-3 align-items-center">
                    <div class="text-white">
                        <h2 class="fs-2">${report.successRate}%</h2>
                        <p class="fs-1fst-italic font-monospace">successful</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="container text-center w-80 p-3">
            <div class="accordion" id="classReport">

                <#list report.classReports as classReport>

                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button bg-light" type="button"
                                    data-bs-toggle="collapse"
                                    data-bs-target="#collapse${classReport?counter}"
                                    aria-expanded="false"
                                    aria-controls="collapse${classReport?counter}">

                                <div class="row justify-content-between w-100 align-items-center p-1 px-4">
                                    <div class="col-6 font-monospace"> ${classReport.name}</div>
                                    <div class="col-1 text-center text-black">
                                        <div class="fs-4"><strong>${classReport.totalCount}</strong></div>
                                        <div class="fs-6 fst-italic font-monospace">tests</div>
                                    </div>

                                    <div class="col-1 text-center text-success">
                                        <div class="fs-5"><strong>${classReport.successCount}</strong></div>
                                        <div class="fs-6 fst-italic font-monospace">passed</div>
                                    </div>
                                    <div class="col-1 text-center text-danger">
                                        <div class="fs-5"><strong>${classReport.failureCount}</strong></div>
                                        <div class="fs-6 fst-italic font-monospace">failed</div>
                                    </div>
                                    <div class="col-1 text-center text-warning">
                                        <div class="fs-5"><strong>${classReport.skippedCount}</strong></div>
                                        <div class="fs-6 fst-italic font-monospace">skipped</div>
                                    </div>

                                </div>
                            </button>
                        </h2>
                        <div id="collapse${classReport?counter}" class="accordion-collapse collapse"
                             data-bs-parent="#classReport">
                            <div class="accordion-body">
                                <div class="container text-center w-80 p-3">
                                    <div class="accordion" id="testReport">
                                        <#list classReport.testReports as testReport>
                                            <div class="accordion-item">
                                                <h2 class="accordion-header">
                                                    <button class="accordion-button bg-light" type="button"
                                                            data-bs-toggle="collapse"
                                                            data-bs-target="#collapse${classReport?counter}${testReport?counter}"
                                                            aria-expanded="false"
                                                            aria-controls="collapse${classReport?counter}${testReport?counter}">

                                                        <div class="row  w-100 align-items-center p-1 px-4">
                                                            <div class=" col font-monospace fs-6">
                                                                ${testReport.name}
                                                            </div>
                                                            <div class=" col font-monospace ${testReport.statusColor} fs-6"> ${testReport.status}</div>
                                                        </div>

                                                    </button>
                                                </h2>
                                                <div id="collapse${classReport?counter}${testReport?counter}"
                                                     class="accordion-collapse collapse"
                                                     data-bs-parent="#testReport">
                                                    <div class="accordion-body">
                                                        <div class="text-start">
                                                            <#list testReport.descriptions as desc>
                                                                <div class="row">
                                                                    <div class="col-1"><strong>${desc.prefix}</strong>
                                                                    </div>
                                                                    <div class="col-11 font-monospace">${desc.label}</div>
                                                                </div>
                                                            </#list>
                                                        </div>

                                                        <#if testReport.failureReason??>

                                                            <div class=" m-3 rounded  bg-danger text-center font-monospace align-items-center">
                                                               <p class="text-white">${testReport.failureReason} </p>
                                                            </div>

                                                            <div class="container">

                                                                <div class="d-flex justify-content-center align-content-center">
                                                                    <button id="showStack" class="btn btn-outline-danger" onclick=showStackTrace("stack${classReport?counter}${testReport?counter}")> show stacktrace</button>
                                                                    <button id="hideStack" class="btn btn-light border berder-black text-black" onclick=hideStacktrace("stack${classReport?counter}${testReport?counter}") style="display: none"> hide stacktrace</button>
                                                                </div>

                                                                <div class="container text-start text-danger rounded border border-danger p-2 mt-3" id="stack${classReport?counter}${testReport?counter}"  style="display: none">
                                                                    <#list testReport.stacktraces as trace>
                                                                        <p>${trace}</p>
                                                                    </#list>
                                                                </div>

                                                            </div>
                                                           </#if>
                                                    </div>
                                                </div>
                                            </div>
                                        </#list>

                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </#list>

            </div>
        </div>

    </div>

</div>


</body>
</html>