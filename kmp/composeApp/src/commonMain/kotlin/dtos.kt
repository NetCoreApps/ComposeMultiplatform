/* Options:
Date: 2024-03-02 17:58:40
Version: 8.12
Tip: To override a DTO option, remove "//" prefix before updating
BaseUrl: https://localhost:5001

//Package: 
//AddServiceStackTypes: True
//AddResponseStatus: False
//AddImplicitVersion: 
//AddDescriptionAsComments: True
//IncludeTypes: 
//ExcludeTypes: 
//InitializeCollections: True
//TreatTypesAsStrings: 
//DefaultImports: java.math.*,java.util.*,net.servicestack.client.*
*/

import java.math.*
import java.util.*
import net.servicestack.client.*


@Route("/hello/{Name}")
open class Hello : IReturn<HelloResponse>, IGet
{
    var name:String? = null
    companion object { private val responseType = HelloResponse::class.java }
    override fun getResponseType(): Any? = Hello.responseType
}

open class SearchFiles : IReturn<SearchFilesResponse>, IGet
{
    var pattern:String? = null
    companion object { private val responseType = SearchFilesResponse::class.java }
    override fun getResponseType(): Any? = SearchFiles.responseType
}

open class HelloResponse
{
    var result:String? = null
}

open class SearchFilesResponse
{
    var results:ArrayList<String> = ArrayList<String>()
}
