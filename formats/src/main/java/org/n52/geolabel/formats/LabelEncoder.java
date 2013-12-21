package org.n52.geolabel.formats;

import java.io.IOException;
import java.io.InputStream;

import org.n52.geolabel.commons.Label;

public interface LabelEncoder {

    public abstract InputStream encode(Label l) throws IOException;

    public abstract InputStream encode(Label l, int size) throws IOException;

    public abstract String getMimeType();

}
