package com.teammental.authorization.service;

import java.util.List;

/**
 * Created by okan on 3.09.2017.
 */
public interface UserRoleService {
  List<CityDto> getAllCityByCountry(Integer countryId);

}
