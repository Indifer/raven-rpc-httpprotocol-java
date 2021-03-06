package raven.rpc.httpprototocol.extension;

import raven.rpc.httpprototocol.entity.ObjectHttpEntity;
import raven.rpc.httpprototocol.formatting.MediaTypeFormatter;
import raven.rpc.httpprototocol.formatting.MediaTypeFormatterCollection;
import org.apache.http.HttpEntity;
import org.apache.http.util.Args;

import java.io.IOException;
import java.util.List;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public class HttpEntitys {

    private static MediaTypeFormatterCollection _defaultMediaTypeFormatterCollection;

    private static MediaTypeFormatterCollection getDefaultMediaTypeFormatterCollection() {
        if (HttpEntitys._defaultMediaTypeFormatterCollection == null)
            HttpEntitys._defaultMediaTypeFormatterCollection = new MediaTypeFormatterCollection();
        return HttpEntitys._defaultMediaTypeFormatterCollection;
    }

    /**
     * payload方式的HttpResponse, read成POJO,  {@link HttpEntity}
     *
     * @param clazz
     * @param httpEntity
     * @param <T>
     * @return T
     * @throws IOException
     */
    public static <T> T readAs(Class<T> clazz, HttpEntity httpEntity) throws IOException {
        return readAs(clazz, httpEntity, HttpEntitys.getDefaultMediaTypeFormatterCollection());
    }

    /**
     * payload方式的HttpResponse, read成POJO
     *
     * @param clazz
     * @param httpEntity
     * @param formatters
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T readAs(Class<T> clazz, HttpEntity httpEntity, List<MediaTypeFormatter> formatters) throws IOException {
        if (httpEntity == null)
            Args.notNull(httpEntity, "httpEntity");
        if (clazz == null)
            Args.notNull(clazz, "clazz");
        if (formatters == null)
            Args.notNull(formatters, "formatters");

        if (httpEntity.getClass().isAssignableFrom(ObjectHttpEntity.class)) {
            ObjectHttpEntity objectHttpEntity = (ObjectHttpEntity) httpEntity;
            if (objectHttpEntity != null) {
                Object value = objectHttpEntity.getValue();
                if (value != null && clazz.isAssignableFrom(value.getClass())) {
                    return (T) value;
                }
            }
        }

        String mediaType = httpEntity.getContentType().getValue().trim().toLowerCase();
//        if(mediaType == null){
//
//        }
//        MediaTypeConstants.ApplicationOctetStreamMediaType;
        MediaTypeFormatter reader = new MediaTypeFormatterCollection(formatters).FindReader(mediaType);
        if (reader != null)
            return HttpEntitys.readAsCore(clazz, httpEntity, reader);

        return null;
    }


    /**
     * @param clazz
     * @param httpEntity
     * @param formatter
     * @param <T>
     * @return
     * @throws IOException
     */
    private static <T> T readAsCore(Class<T> clazz, HttpEntity httpEntity, MediaTypeFormatter formatter) throws IOException {
        return formatter.readFrom(clazz, httpEntity);
    }
}
