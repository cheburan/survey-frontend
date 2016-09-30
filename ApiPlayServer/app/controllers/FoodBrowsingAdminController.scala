/*
This file is part of Intake24.

Copyright 2015, 2016 Newcastle University.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package controllers

import scala.concurrent.Future

import javax.inject.Inject
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Controller
import security.DeadboltActionsAdapter
import security.Roles
import uk.ac.ncl.openlab.intake24.services.fooddb.admin.FoodBrowsingAdminService

class FoodBrowsingAdminController @Inject() (service: FoodBrowsingAdminService, deadbolt: DeadboltActionsAdapter) extends Controller
    with PickleErrorHandler
    with ApiErrorHandler {

  def getUncategorisedFoods(locale: String) = deadbolt.restrict(Roles.superuser) {
    Future {
      translateError(service.getUncategorisedFoods(locale))
    }
  }

  def getRootCategories(locale: String) = deadbolt.restrict(Roles.superuser) {
    Future {
      translateError(service.getRootCategories(locale))
    }
  }

  def getCategoryContents(code: String, locale: String) = deadbolt.restrict(Roles.superuser) {
    Future {
      translateError(service.getCategoryContents(code, locale))
    }
  }

  def getFoodParentCategories(code: String, locale: String) = deadbolt.restrict(Roles.superuser) {
    Future {
      translateError(service.getFoodParentCategories(code, locale))
    }
  }

  def getFoodAllCategories(code: String, locale: String) = deadbolt.restrict(Roles.superuser) {
    Future {
      translateError(service.getFoodAllCategoriesHeaders(code, locale))
    }
  }

  def getCategoryParentCategories(code: String, locale: String) = deadbolt.restrict(Roles.superuser) {
    Future {
      translateError(service.getCategoryParentCategories(code, locale))
    }
  }

  def getCategoryAllCategories(code: String, locale: String) = deadbolt.restrict(Roles.superuser) {
    Future {
      translateError(service.getCategoryAllCategoriesHeaders(code, locale))
    }
  }

}