
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;


  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
    GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {

  LocalTime time = currentTime;

  boolean isPeak =
      (!time.isBefore(LocalTime.of(8, 0)) && time.isBefore(LocalTime.of(10, 1)))   // 8:00 to 10:00 inclusive
      || (!time.isBefore(LocalTime.of(13, 0)) && time.isBefore(LocalTime.of(14, 1))) // 13:00 to 14:00 inclusive
      || (!time.isBefore(LocalTime.of(19, 0)) && time.isBefore(LocalTime.of(21, 1))); // 19:00 to 21:00 inclusive

  double servingRadius = isPeak ? peakHoursServingRadiusInKms : normalHoursServingRadiusInKms;

  List<Restaurant> restaurant = restaurantRepositoryService.findAllRestaurantsCloseBy(
      getRestaurantsRequest.getLatitude(),
      getRestaurantsRequest.getLongitude(),
      currentTime,
      servingRadius);

  GetRestaurantsResponse response = new GetRestaurantsResponse(restaurant);
  log.info(response);
  return response;
}
}
