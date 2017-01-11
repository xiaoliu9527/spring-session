/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import sample.client.Application;
import sample.pages.HomePage;
import sample.pages.HomePage.Attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Eddú Meléndez
 * @author Rob Winch
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AttributeTests {

	@Autowired
	private MockMvc mockMvc;

	private WebDriver driver;

	@Before
	public void setup() {
		this.driver = MockMvcHtmlUnitDriverBuilder
				.mockMvcSetup(this.mockMvc)
				.build();
	}

	@After
	public void tearDown() {
		this.driver.quit();
	}

	@Test
	public void home() {
		HomePage home = HomePage.go(this.driver);
		home.assertAt();
	}

	@Test
	public void noAttributes() {
		HomePage home = HomePage.go(this.driver);
		assertThat(home.attributes()).isEmpty();
	}

	@Test
	public void createAttribute() {
		HomePage home = HomePage.go(this.driver);
		// @formatter:off
		home = home.form()
				.attributeName("a")
				.attributeValue("b")
				.submit(HomePage.class);
		// @formatter:on

		List<Attribute> attributes = home.attributes();
		assertThat(attributes).hasSize(2);
		assertThat(attributes.get(0).getAttributeName()).isEqualTo("requestCount");
		assertThat(attributes.get(0).getAttributeValue()).isEqualTo("1");
		assertThat(attributes.get(1).getAttributeName()).isEqualTo("a");
		assertThat(attributes.get(1).getAttributeValue()).isEqualTo("b");
	}

}
