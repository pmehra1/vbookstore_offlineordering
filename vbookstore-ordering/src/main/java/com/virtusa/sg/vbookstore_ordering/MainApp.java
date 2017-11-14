package com.virtusa.sg.vbookstore_ordering;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.main.Main;

import com.virtusa.sg.vbookstore_billing.routes.BillingRouteBuilder;
import com.virtusa.sg.vbookstore_billing.routes.BillingSummaryRouteBuilder;
import com.virtusa.sg.vbookstore_orders.routes.CSVRouteBuilder;
import com.virtusa.sg.vbookstore_orders.routes.EmailRouteBuilder;
import com.virtusa.sg.vbookstore_orders.routes.FTPRouteBuilder;

/**
 * VBookstore Offline Ordering Application
 */
public class MainApp {

	/**
	 * A main() method to start the application and routes
	 */
	public static void main(String... args) throws Exception {
		Main main = new Main();

		main.addRouteBuilder(new EmailRouteBuilder());
		main.addRouteBuilder(new CSVRouteBuilder());
		main.addRouteBuilder(new FTPRouteBuilder());
		main.addRouteBuilder(new BillingRouteBuilder());
		main.addRouteBuilder(new BillingSummaryRouteBuilder());

		main.bind("jms", JmsComponent.jmsComponentAutoAcknowledge(createConnectionFactoryPool()));

		main.run(args);
	}

	private static PooledConnectionFactory createConnectionFactoryPool() {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
		PooledConnectionFactory pooledConnFactory = new PooledConnectionFactory();
		pooledConnFactory.setConnectionFactory(connectionFactory);
		pooledConnFactory.setMaxConnections(10);

		return pooledConnFactory;
	}

}
