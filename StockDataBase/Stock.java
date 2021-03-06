
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Stock implements Comparable<Stock>
{
	private static Logger log = LogManager.getLogger();

	public static int cnttt = 0;
	public boolean DEBUG = false;
	private Path dailyPriceCSV;
	private final String NAME;
	private final String ALIAS;
	private boolean isValid;
	private String earningDate;
	private String EPS_text;
	private EPS eps;
	private String range52;
	public String FAILS = "";

	private Double year_High;
	private Double year_Low;
	private Double year_Average;
	private Double monthAverage;
	private Double weekAverage;
	private Double dualWeekAverage;

	private DecimalFormat twoDecimalFormatter = new DecimalFormat(".##");

	public Stock(String alias, String name, String earningDate, String range52, String EPS_text, Path csvPath) throws IOException
	{
		if (!alias.equals("") && !name.equals("") && !earningDate.equals("") && !EPS_text.equals("") && !range52.equals("") && csvPath != null)
		{
			// long start = System.currentTimeMillis();
			isValid = true;
			ALIAS = alias;
			NAME = name;
			this.earningDate = earningDate;
			this.range52 = range52;
			this.EPS_text = EPS_text;
			this.dailyPriceCSV = csvPath;
			setEPS();
			processCSV();
			// long end = System.currentTimeMillis();
			// System.out.println("Took " + (end - start)/1000.0 + " to create
			// stock object");
			log.trace("Success!\n");
		} else
		{
			isValid = false;
			if (name.length() > 100)
			{
				NAME = "BAD NAME";
			} else
			{
				NAME = name;
			}
			ALIAS = alias;
			if (earningDate.equals(""))
			{
				FAILS += "ED,";
			}
			if (EPS_text.equals(""))
			{
				FAILS += "EPS,";
			}
			if (range52.equals(""))
			{
				FAILS += "RANGE,";
			}
			if (csvPath == null)
			{
				FAILS += "PATH,";
			}
			if (FAILS.length() > 1)
			{
				FAILS += "\b";
			}

			log.warn("Failed! Stock invalid, " + alias + "|" + name + "|" + earningDate + "|" + range52 + "|" + EPS_text + "|" + csvPath);
		}
	}

	@Override
	public String toString()
	{
		String output = "";
		// System.out.println(weekAverage + " " + dualWeekAverage + " " +
		// year_Average);
		if (weekAverage != null && dualWeekAverage != null && year_Average != null)
		{
			output += ALIAS + "|" + NAME + "|" + twoDecimalFormatter.format(weekAverage) + "  " + twoDecimalFormatter.format(dualWeekAverage) + "  " + twoDecimalFormatter.format(year_Average) + "  ";
		} else
		{
			output += ALIAS + "|" + NAME + "|BAD DATA";

		}
		return output;
	}

	@Override
	public int compareTo(Stock stock)
	{
		return ALIAS.compareTo(stock.ALIAS);
	}

	private boolean setEPS()
	{
		ArrayList<Double> doubles = new ArrayList<>();
		String split[] = EPS_text.split(",");
		boolean complete = true;
		for (String s: split)
		{
			if (s.equals("N/A") || s.isEmpty() || s == null)
			{
				doubles.add(null);
				complete = false;
			} else
			{
				doubles.add(Double.parseDouble(s));
			}
		}
		eps = new EPS(doubles);
		return complete;
	}

	private void processCSV() throws IOException
	{
		if (dailyPriceCSV != null)
		{
			BufferedReader reader = Files.newBufferedReader(dailyPriceCSV, StandardCharsets.UTF_8);
			reader.readLine();
			String line = "";
			ArrayList<Double> close = new ArrayList<>();
			while ((line = reader.readLine()) != null)
			{
				String content[] = line.split(",");
				close.add(Double.parseDouble(content[4]));
			}

			double sum = 0;
			for (int i = 0; i < Math.min(365, close.size()); i++)
			{
				sum += close.get(i);
				// System.out.print(sum + ",");
				if (i == 4)
				{
					weekAverage = sum / 5.0;
				} // System.out.println("week avg:" + weekAverage);}
				if (i == 9)
				{
					dualWeekAverage = sum / 10.0;
				} // System.out.println("dweek avg:" + dualWeekAverage);}
				if (i == 19)
				{
					monthAverage = sum / 20.0;
				}
			}
			year_Average = sum / close.size();
			reader.close();
		}
	}

	public Path getDailyPriceCSV()
	{
		return dailyPriceCSV;
	}

	public String getNAME()
	{
		return NAME;
	}

	public String getALIAS()
	{
		return ALIAS;
	}

	public boolean isValid()
	{
		return isValid;
	}

	public String getEarningDate()
	{
		return earningDate;
	}

	public String getEPS_text()
	{
		return EPS_text;
	}

	public EPS getEps()
	{
		return eps;
	}

	public String getRange52()
	{
		return range52;
	}

	public Double getYear_High()
	{
		return year_High;
	}

	public Double getYear_Low()
	{
		return year_Low;
	}

	public Double getYear_Average()
	{
		return year_Average;
	}

	public Double getMonthAverage()
	{
		return monthAverage;
	}

	public Double getWeekAverage()
	{
		return weekAverage;
	}

	public Double getDualWeekAverage()
	{
		return dualWeekAverage;
	}

}
