import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;


public class CodecTest {

    /**
     * @param args
     * @throws EncoderException 
     */
    public static void main(String[] args) throws EncoderException {
        URLCodec urlCodec = new URLCodec();
        System.out.println(urlCodec.encode("dolf's"));
        System.out.println(urlCodec.encode("dolf\"s"));
        System.out.println(urlCodec.encode("dolf&s"));
        System.out.println(urlCodec.encode("dolf#s"));
    }

}
