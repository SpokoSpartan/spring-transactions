package com.spot.on.transactions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {

	private final TestRepository repository;

	/***
	 * @Transactional on public method but with two AOP proxies (annotations that requires AOP).
	 * It might be tricky, but even when having @Transactional on public direct method it might not work.
	 * It's caused because different aspect is currently on this method -> @PostConstruct
	 */
	@Transactional
	@PostConstruct
	public void initAnimal() {
		log.info("Transaction status: " + TransactionSynchronizationManager.isActualTransactionActive());
		// "Printed -> Transaction status: false"
		Animal animal = new Animal();
		animal.setName("Piggy");
		animal.setAge(1);
		repository.save(animal);
	}

	/***
	 * @Transactional on public method.
	 * The only way of configuring transaction in Spring
	 * Method need to be public and need to be called directly
	 */
	@Transactional
	public void changeAnimalName_public() {
		log.info("Transaction status: " + TransactionSynchronizationManager.isActualTransactionActive());
		// "Printed -> Transaction status: true"
		Animal animal = repository.findById(1L).orElseThrow();
		animal.setAge(100);
	}

	/**
	 * @Transactional on protected method.
	 * Technically it is possible to create transaction on protected method.
	 * CGLib (proxy used by Spring) extends base class and thought public and protected methods can be extended.
	 * However it's not supported in Spring AOP that is used to create transaction .
	 * Creating proxy over protected method is not recommended.
	 */
	@Transactional
	protected void changeAnimalName_protected() {
		log.info("Transaction status: " + TransactionSynchronizationManager.isActualTransactionActive());
		// "Printed -> Transaction status: false"
		Animal animal = repository.findById(1L).orElseThrow();
		animal.setAge(200);
	}

	/**
	 * @Transactional on private method.
	 * CGLib (Spring proxy) extends base class and thought public and protected methods can be extended.
	 * So it's impossible to create proxy here.
	 */
	@Transactional
	private void changeAnimalName_private() {
		log.info("Transaction status: " + TransactionSynchronizationManager.isActualTransactionActive());
		// "Not callable from controller -> Transaction status: false"
		Animal animal = repository.findById(1L).orElseThrow();
		animal.setAge(300);
	}

	/**
	 * @Transactional on public indirect method.
	 * Self-invocation i.e. a method within the target object calling some other method of the target object
	 * This won't lead to an actual transaction at runtime even if the invoked method is marked with @Transactional!
	 * In proxy mode (the default), only 'external' method calls coming in through the proxy will be intercepted.
	 */
	public void changeAnimalName_indirect_public() {
		change_public();
	}
	@Transactional
	public void change_public() {
		log.info("Transaction status: " + TransactionSynchronizationManager.isActualTransactionActive());
		// "Printed -> Transaction status: false"
		Animal animal = repository.findById(1L).orElseThrow();
		animal.setAge(400);
	}

}
