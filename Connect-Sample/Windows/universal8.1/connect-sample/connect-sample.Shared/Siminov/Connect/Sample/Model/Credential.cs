﻿///
/// [SIMINOV FRAMEWORK]
/// Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.



using System;
using System.Collections.Generic;
using System.Text;

namespace Siminov.Connect.Sample.Model
{
    public class Credential : Core.Database.Database
    {
        //Column Names.
	    public static readonly String ACCOUNT_ID = "ACCOUNT_ID";
	    public static readonly String TOKEN = "TOKEN";
        public static readonly String ACTIVE = "ACTIVE";


	    //Class Variables.
	    private String accountId = null;
	    private String token = null;
	    private bool active;

	    public String GetAccountId() 
        {
		    return this.accountId;
	    }

	    public void SetAccountId(String accountId) 
        {
		    this.accountId = accountId;
	    }

	    public String GetToken() 
        {
		    return this.token;
	    }

	    public void SetToken(String token) 
        {
		    this.token = token;
	    }

	    public bool GetActive() 
        {
		    return this.active;
	    }
	
	    public bool IsActive() 
        {
		    return this.active;
	    }

	    public void SetActive(bool active) 
        {
		    this.active = active;
	    }

    }
}
