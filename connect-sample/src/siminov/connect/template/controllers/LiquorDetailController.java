/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2015] [Siminov Software Solution LLP|support@siminov.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package siminov.connect.template.controllers;

import siminov.connect.template.model.LiquorBrand;
import siminov.orm.exception.DatabaseException;
import siminov.orm.log.Log;

public class LiquorDetailController {

	public LiquorBrand[] getLiquorBrands(String liquorType) {
		
		try {
			return (LiquorBrand[]) new LiquorBrand().select().where(LiquorBrand.LIQUOR_TYPE).equalTo(liquorType).execute();
		} catch(DatabaseException databaseException) {
			Log.error(getClass().getName(), "getLiquorBrands", "DatabaseException caught while getting liquor brands, " + databaseException.getMessage());
			return null;
		}
	}
	
}