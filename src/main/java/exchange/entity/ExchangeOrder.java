package exchange.entity;

import java.util.Date;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ExchangeOrder{

	private long id;
	private Date placedAt;
	private Currency currency;
	private double volume;
	private double priceInConventionalUnits;
	private Action action;
	private double priceForUnit;
	public static enum Action{
		BUY, SELL
	}
	
	/*@Override
	public int compare(ExchangeOrder o1, ExchangeOrder o2) {
		if (o1.getPriceForUnit() > o2.getPriceForUnit())
			return 1;
		if (o1.getPriceForUnit() < o2.getPriceForUnit())
			return -1;
		return 0;
	} */
}
