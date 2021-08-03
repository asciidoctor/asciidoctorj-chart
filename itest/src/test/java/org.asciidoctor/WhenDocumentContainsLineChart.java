package org.asciidoctor;


import org.hamcrest.Matchers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WhenDocumentContainsLineChart {

    private Asciidoctor asciidoctor;

    @BeforeEach
    void beforeEach() {
        this.asciidoctor = Asciidoctor.Factory.create();
    }

    @Test
    void script_block_should_added_to_output_document(@TempDir File outputDir) throws IOException {

        File inputFile = new File("src/test/resources/line-chart.adoc");
        asciidoctor.requireLibrary("asciidoctor-chart");
        asciidoctor.convertFile(inputFile, Options.builder().safe(SafeMode.UNSAFE).backend("html5").toDir(outputDir.getAbsoluteFile()).baseDir(new File("src/test/resources")).destinationDir(outputDir.getAbsoluteFile()).build());

        File lineChartOutputFile = new File(outputDir, "line-chart.html");

        assertTrue(lineChartOutputFile.exists());
        Document doc = Jsoup.parse(lineChartOutputFile, "UTF-8");
        List<String> scriptList = doc.head().getElementsByTag("script").stream().map(element -> element.attr("src")).collect(Collectors.toList());

        assertThat(scriptList, hasItem("http://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js"));
        Element element = doc.body().selectFirst("div > script");
        assertThat(element, Matchers.notNullValue());
    }
}
