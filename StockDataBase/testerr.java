
import java.nio.file.Path;
import java.nio.file.Paths;

public class testerr
{
	public static void main(String args[])
	{
		String arg[] =
		{ "-p", "test\\Nasdaq300" };
		Path write = Paths.get("../output.csv");

		try
		{
			CSVhandler.write(write, StockBuilder.build(Paths.get(arg[1])));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
