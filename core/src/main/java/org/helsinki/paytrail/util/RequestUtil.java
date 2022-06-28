package org.helsinki.paytrail.util;

import okhttp3.RequestBody;
import okio.Buffer;

import java.io.IOException;

public class RequestUtil {
    public static String bodyToString(final RequestBody request){
        try {
            final Buffer buffer = new Buffer();
            request.writeTo(buffer);
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }
}
