package myudfs;

import java.io.IOException;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.util.WrappedIOException;

public class Ucfirst extends EvalFunc<String> {
    public String exec(Tuple input) throws IOException {
        if (input.size() == 0)
            return null;
        try {
            String str = (String) input.get(0);
            char ch = str.toUpperCase().charAt(0);
            String str1 = String.valueOf(ch);
            return str1;

        } catch (Exception e) {
            // TODO: handle exception
            throw WrappedIOException.wrap(
                    "Caught exception processing input row ", e);
        }
    }

}
