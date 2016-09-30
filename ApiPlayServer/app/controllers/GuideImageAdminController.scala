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

import be.objectify.deadbolt.scala.DeadboltActions
import javax.inject.Inject
import play.api.mvc.Action
import play.api.mvc.Controller
import security.Roles
import uk.ac.ncl.openlab.intake24.services.fooddb.admin.GuideImageAdminService
import security.DeadboltActionsAdapter
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class GuideImageAdminController @Inject() (service: GuideImageAdminService, deadbolt: DeadboltActionsAdapter) extends Controller
    with PickleErrorHandler
    with ApiErrorHandler {
  
   def listGuideImages() = deadbolt.restrict(Roles.superuser) {
    Future {
      translateError(service.listGuideImages())
    }
  }
   
   def getGuideImage(id: String) = deadbolt.restrict(Roles.superuser) {
    Future {
      translateError(service.getGuideImage(id))
    }
  } 
}