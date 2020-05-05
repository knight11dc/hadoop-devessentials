package pocudf;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;

@Description(
	name = "ed_year",
	value = "_FUNC_(str) - Get the integer year of the education",
	extended = "Example:\n" +
	"  > SELECT ed_year(12-14years) FROM authors a;\n" +
	"  13"
	)
public class ed_year extends UDF {

    public IntWritable evaluate(Text s) {
		IntWritable to_value = new IntWritable();
		if (s != null) {
		                     try {
                                int out = 0;

                                String val=s.toString().replace("years","");
                                if(!val.matches(".*[<>-].*"))
                                        out = Integer.parseInt(val);
                                to_value.set(out);

                                if( val.charAt(0)=='<'){
                                        out= Integer.parseInt(val.replace("<",""))-1;
                                        to_value.set(out);
                                }
                                if( val.charAt(0)=='>'){
                                        out= Integer.parseInt(val.replace(">",""))+1;
                                        to_value.set(out);
                                }

                                 int index = val.indexOf('-');
                                 if(index != -1 )
                                 {
                                         int x = Integer.parseInt(val.substring(0, index));
                     int y = Integer.parseInt(val.substring(index+1));
                     out = (x+y)/2;
                     to_value.set(out);
                                         return to_value;
                                 }
                        }
                                catch (Exception e) { // Should never happen

                                to_value = null;
                    }
                }
                return to_value;
    }
}
