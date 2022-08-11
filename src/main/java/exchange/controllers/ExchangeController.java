package exchange.controllers;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.http.MediaType;
import exchange.data.OrderRepository;
import exchange.entity.Currency;
import exchange.entity.ExchangeOrder;
import exchange.entity.ExchangeOrder.Action;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/exchange")
@Tag(name = "exchange", description = "exchange api")
public class ExchangeController {

	public static final Logger log = LoggerFactory.getLogger(ExchangeController.class);
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Hidden
	@GetMapping("/index")
    public String index(){
        return "index";
    }
	
	@Operation(summary = "Shows list or orders")
	@GetMapping(value = "/show_orders")//, produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Flux<ExchangeOrder> showOrders(){
		Flux<ExchangeOrder> result = orderRepository.orderList();
		return result.delayElements(Duration.ofSeconds(3))
				.doOnNext(val -> log.info("Data: " + val))
				.doOnComplete(() -> log.info("completed"));
	}	
	
	@Operation(summary = "Create order")
	//@RequestBody
	@PostMapping("/create_order")
	public @ResponseBody Mono<ExchangeOrder> saveOrder(
			@RequestParam Currency currency,
			@RequestParam double volume,
			@RequestParam double priceInConventionalUnits,
			@RequestParam Action action) {
		ExchangeOrder order = new ExchangeOrder();
		order.setAction(action);
		order.setCurrency(currency);
		order.setVolume(volume);
		order.setPriceInConventionalUnits(priceInConventionalUnits);
		order = orderRepository.save(order);
		return Mono.just(order);
	}
	
	@GetMapping("/show_by_id")
	@Operation(summary = "Show order with specified id")
	//@Parameter(name = "order id")
	public Mono<ExchangeOrder> showOne(@RequestParam long id) {
		return Mono.just(orderRepository.showOrderById(id));
	}
	

	@Operation(summary = "Delete order with specified id")
	//@Parameter(name = "order id")
	@PostMapping("/delete")
	public String deleteOrderById(@RequestParam long id) {
		ExchangeOrder order = orderRepository.deleteOrderById(id);
		if (order == null) {
			return "Not existing id";
		} else {
			return "Deleted order id " + order.getId();
		}
	}
}
