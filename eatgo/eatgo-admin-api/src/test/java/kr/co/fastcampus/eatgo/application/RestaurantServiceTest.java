package kr.co.fastcampus.eatgo.application;

import kr.co.fastcampus.eatgo.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class RestaurantServiceTest {
    private RestaurantService restaurantService;
    @Mock
    private RestaurantRepository restaurantRepository;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        MockRestaurantRepository();
        restaurantService=new RestaurantService(restaurantRepository);

    }

    private void MockRestaurantRepository() {
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant restaurant= Restaurant.builder().id(1004L).categoryId(1L).name("Bob zip").address("Seoul").build();
        restaurants.add(restaurant);
        given(restaurantRepository.findAll()).willReturn(restaurants);
        given(restaurantRepository.findById(1004L)).willReturn(Optional.of(restaurant));
    }

    @Test
    public void getRestaurantwithExisted(){
        Restaurant restaurant = restaurantService.getRestaurant(1004L);

            assertThat(restaurant.getId(), is(1004L));
    }

    @Test(expected= RestaurantNotFoundException.class)
    public void getRestaurantwithNotExisted(){
        Restaurant restaurant = restaurantService.getRestaurant(404L);
        }

    @Test
    public void getRestaurants(){
        List<Restaurant> restaurants = restaurantService.getRestaurants();

        Restaurant restaurant=restaurants.get(0);
        assertThat(restaurant.getId(), is(1004L));
    }
    @Test
    public void addRestaurant(){
        given(restaurantRepository.save(any())).will(invocation->{
            Restaurant restaurant = invocation.getArgument(0);
            restaurant.setId(1234L);
            return restaurant;
        });
        Restaurant restaurant=Restaurant.builder().name("BeRyoung").address("Busan").build();
        Restaurant created = restaurantService.addRestaurant(restaurant);

        assertThat(created.getId(), is(1234L));
    }
    @Test
    public void updateRestaurant(){

        Restaurant restaurant = Restaurant.builder().id(1004L).name("bobzip").address("seoul").build();
        given(restaurantRepository.findById(1004L)).willReturn(Optional.of(restaurant));
        restaurantService.updateRestaurant(1004L, "sulzip", "busan");
        assertThat(restaurant.getName(),is("sulzip"));
        assertThat(restaurant.getAddress(),is("busan"));
    }
}