
package org.n52.geolabel.formats;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.LabelFacet.Availability;

public class PngTest {

    private PngEncoder encoder;

    @Before
    public void prepare() {
        this.encoder = new PngEncoder();
    }

    @Test
    public void saveImageFromSVG() throws IOException {
        InputStream svg = getClass().getResourceAsStream("/label.svg");

        InputStream in = this.encoder.encode(svg);

        File temp = File.createTempFile("geolabel_", ".png");
        try (FileOutputStream tempFile = new FileOutputStream(temp);) {
        IOUtils.copy(in, tempFile);
        }

        // System.out.println("Saved geolabel as " + temp.getAbsolutePath());

        Long actual = Long.valueOf(Files.size(temp.toPath()));
        assertThat("file is larger than 0 KB", actual, is(greaterThan(Long.valueOf(0l))));

        temp.deleteOnExit();
    }

    @Test
    public void saveEmptyLabel() throws IOException {
        Label l = new Label();

        InputStream in = this.encoder.encode(l);

        File temp = File.createTempFile("geolabel_", ".png");
        try (FileOutputStream tempFile = new FileOutputStream(temp);) {
            IOUtils.copy(in, tempFile);
        }

        // System.out.println("Saved empty geolabel as " + temp.getAbsolutePath());

        Long actual = Long.valueOf(Files.size(temp.toPath()));
        assertThat("file is larger than 0 KB", actual, is(greaterThan(Long.valueOf(0l))));

        temp.deleteOnExit();
    }

    @Test
    public void sizeParameterIsUsed() throws IOException {
        Label l1 = new Label();
        InputStream inSmall = this.encoder.encode(l1, 64);
        Label l2 = new Label();
        InputStream inLarge = this.encoder.encode(l2, 512);

        File tempSmall = File.createTempFile("geolabel_", ".png");
        try (FileOutputStream tempFileSmall = new FileOutputStream(tempSmall);) {
            IOUtils.copy(inSmall, tempFileSmall);
        }

        File tempLarge = File.createTempFile("geolabel_", ".png");
        try (FileOutputStream tempFileLarge = new FileOutputStream(tempLarge);) {
            IOUtils.copy(inLarge, tempFileLarge);
        }

        // System.out.printf("Saved small and larg geolabel as %s and %s",
        // tempSmall.getAbsolutePath(),
        // tempLarge.getAbsolutePath());

        Long small = Long.valueOf(Files.size(tempSmall.toPath()));
        Long large = Long.valueOf(Files.size(tempLarge.toPath()));
        assertThat("file size of larger label is larger", large, is(greaterThan(small)));

        tempSmall.deleteOnExit();
        tempLarge.deleteOnExit();
    }

    @Test
    public void emptyLabelIsSmallerThanFullLabel() throws IOException {
        Label empty = new Label();
        InputStream inSmall = this.encoder.encode(empty);

        Label full = new Label();
        full.getCitationsFacet().updateAvailability(Availability.AVAILABLE);
        full.getExpertFeedbackFacet().updateAvailability(Availability.AVAILABLE);
        full.getLineageFacet().updateAvailability(Availability.AVAILABLE);
        full.getProducerCommentsFacet().updateAvailability(Availability.AVAILABLE);
        full.getProducerProfileFacet().updateAvailability(Availability.AVAILABLE);
        full.getQualityInformationFacet().updateAvailability(Availability.AVAILABLE);
        full.getStandardsComplianceFacet().updateAvailability(Availability.AVAILABLE);
        full.getUserFeedbackFacet().updateAvailability(Availability.AVAILABLE);
        InputStream inLarge = this.encoder.encode(full);

        File tempSmall = File.createTempFile("geolabel_", ".png");
        try (FileOutputStream tempFileSmall = new FileOutputStream(tempSmall);) {
            IOUtils.copy(inSmall, tempFileSmall);
        }

        File tempLarge = File.createTempFile("geolabel_", ".png");
        try (FileOutputStream tempFileLarge = new FileOutputStream(tempLarge);) {
            IOUtils.copy(inLarge, tempFileLarge);
        }

        // System.out.printf("Saved small and larg geolabel as %s and %s",
        // tempSmall.getAbsolutePath(),
        // tempLarge.getAbsolutePath());

        Long small = Long.valueOf(Files.size(tempSmall.toPath()));
        Long large = Long.valueOf(Files.size(tempLarge.toPath()));
        assertThat("file size of full label is larger than empty label", large, is(greaterThan(small)));

        tempSmall.deleteOnExit();
        tempLarge.deleteOnExit();
    }

}
