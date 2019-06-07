package com.training.platform;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.platform.entities.User;
import com.training.platform.repositories.UserRepository;
import com.training.platform.services.UserServiceImpl;
import com.training.platform.services.UtilsServiceImpl;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PlatformApplicationTests extends TestCase {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UtilsServiceImpl utilsService;

	@InjectMocks
	private UserServiceImpl userService;

	private Map<String,String> input;
	private User user;

	@Before
	public void init() {
		input = new HashMap<>();
		input.put("age","29");
		user = User.builder()
				.id(1)
				.age(29)
				.active(1)
				.address("กรุงเทพ")
				.email("test@gmail.com")
				.name("Unittest")
				.build();
	}

	@Test
	public void testSaveUser()  throws Exception{
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
		//Mockito.doNothing().when(utilsService.encrytePassword(Mockito.any()));
		Mockito.when(utilsService.encrytePassword(Mockito.any())).thenReturn(null);
		User returnUser = userService.save(input);
		assertEquals(user,returnUser);
	}

}
