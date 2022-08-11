package exchange.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import exchange.entity.Currency;
import exchange.entity.ExchangeOrder;
import exchange.entity.ExchangeOrder.Action;
import reactor.core.publisher.Flux;

@Repository
public class OrderRepository {

	private AtomicLong id;
	
	private ConcurrentHashMap<Long, ExchangeOrder> orders;
	
	private Flux<ExchangeOrder> flux;
	
	public OrderRepository() {
		this.id = new AtomicLong();
		this.orders = new ConcurrentHashMap<Long, ExchangeOrder>();
		//тестовые данные
		this.flux = Flux.fromIterable(orders.values());
		ExchangeOrder order = new ExchangeOrder();
		order.setId(id.longValue());
		order.setPlacedAt(new Date());
		order.setCurrency(Currency.RUB);
		order.setVolume(16000);
		order.setPriceInConventionalUnits(200);
		order.setAction(Action.BUY);
		order.setPriceForUnit(order.getPriceInConventionalUnits() / order.getVolume());
		orders.put(id.longValue(), order);
		id.incrementAndGet();
		order = new ExchangeOrder();
		order.setId(id.longValue());
		order.setPlacedAt(new Date());
		order.setCurrency(Currency.RUB);
		order.setVolume(20000);
		order.setPriceInConventionalUnits(230);
		order.setAction(Action.BUY);
		order.setPriceForUnit(order.getPriceInConventionalUnits() / order.getVolume());
		orders.put(id.longValue(), order);
		id.incrementAndGet();
		order = new ExchangeOrder();
		order.setId(id.longValue());
		order.setPlacedAt(new Date());
		order.setCurrency(Currency.RUB);
		order.setVolume(105000);
		order.setPriceInConventionalUnits(1456);
		order.setAction(Action.SELL);
		order.setPriceForUnit(order.getPriceInConventionalUnits() / order.getVolume());
		orders.put(id.longValue(), order);
		id.incrementAndGet();
	}
	
	public ExchangeOrder save(ExchangeOrder order) {
		order.setPlacedAt(new Date());
		order.setId(id.getAndIncrement());
		order.setPriceForUnit(order.getPriceInConventionalUnits() / order.getVolume());
		orders.put(order.getId(), order);
		return order;
	}
	
	public Flux<ExchangeOrder> orderList(){
		/*
		List<ExchangeOrder> exchangeOrders = new ArrayList<ExchangeOrder>(orders.values());
		exchangeOrders.sort(new Comparator<ExchangeOrder>(){
			public int compare(ExchangeOrder o1, ExchangeOrder o2) {
				if (o1.getPriceForUnit() > o2.getPriceForUnit())
					return 1;
				if (o1.getPriceForUnit() < o2.getPriceForUnit())
					return -1;
				return 0;
			}
		});
		return exchangeOrders; */
		return flux;
	}
	public ExchangeOrder showOrderById(long id) {
		return orders.get(id);
	}
	
	public ExchangeOrder deleteOrderById(long id) {
		return orders.remove(id);
	}
}
