# Unirest for Android

This is an android port of unirest-java project (https://github.com/Mashape/unirest-java). It uses localized copies of the latest Apache HTTP Components which are not yet available on MavenCentral repo for android projects.

Unirest is a set of lightweight HTTP libraries available in multiple languages, ideal for most applications:

* Make `GET`, `POST`, `PUT`, `PATCH`, `DELETE` requests
* Both syncronous and asynchronous (non-blocking) requests
* It supports form parameters, file uploads and custom body entities
* Supports gzip
* Supports Basic Authentication natively
* Customizable timeout
* Customizable default headers for every request (DRY)
* Customizable `HttpClient` and `HttpAsyncClient` implementation
* Automatic JSON parsing into a native object for JSON responses

Created with love by [thefosk](https://github.com/thefosk) @ [mashape.com](https://mashape.com)

Ported by: Zeeshan Ejaz Bhatti

## Installing
Currently, you must download the project and create AAR file from it to include in your android project. We shall soon upload this to a Maven repo for your ease.
## Asynchronous Requests
Sometimes, well most of the time, you want your application to be asynchronous and not block, Unirest supports this in Java using anonymous callbacks, or direct method placement:

```java
Future<HttpResponse<JsonNode>> future = Unirest.post("http://httpbin.org/post")
  .header("accept", "application/json")
  .field("param1", "value1")
  .field("param2", "value2")
  .asJsonAsync(new Callback<JsonNode>() {
	  
	public void failed(UnirestException e) {
		System.out.println("The request has failed");
	}
	
	public void completed(HttpResponse<JsonNode> response) {
		 int code = response.getCode();
	     Map<String, String> headers = response.getHeaders();
	     JsonNode body = response.getBody();
	     InputStream rawBody = response.getRawBody();
	}
	
	public void cancelled() {
		System.out.println("The request has been cancelled");
	}
	
});
```

## File Uploads
Creating `multipart` requests with Java is trivial, simply pass along a `File` Object as a field:

```java
HttpResponse<JsonNode> jsonResponse = Unirest.post("http://httpbin.org/post")
  .header("accept", "application/json")
  .field("parameter", "value")
  .field("file", new File("/tmp/file"))
  .asJson();
```

## Custom Entity Body

```java
HttpResponse<JsonNode> jsonResponse = Unirest.post("http://httpbin.org/post")
  .header("accept", "application/json")
  .body("{\"parameter\":\"value\", \"foo\":\"bar\"}")
  .asJson();
```

## Basic Authentication
Authenticating the request with basic authentication can be done by calling the `basicAuth(username, password)` function:
```java
HttpResponse<JsonNode> response = Unirest.get("http://httpbin.org/headers").basicAuth("username", "password").asJson();
```

# Request

The Java Unirest library follows the builder style conventions. You start building your request by creating a `HttpRequest` object using one of the following:

```java
HttpRequest request = Unirest.get(String url);
HttpRequestWithBody request = Unirest.post(String url);
HttpRequestWithBody request = Unirest.put(String url);
HttpRequestWithBody request = Unirest.patch(String url);
HttpRequestWithBody request = Unirest.delete(String url);
```

# Response

Upon recieving a response Unirest returns the result in the form of an Object, this object should always have the same keys for each language regarding to the response details.

- `.getCode()` - HTTP Response Status Code (Example 200)
- `.getHeaders()` - HTTP Response Headers
- `.getBody()` - Parsed response body where applicable, for example JSON responses are parsed to Objects / Associative Arrays.
- `.getRawBody()` - Un-parsed response body

# Advanced Configuration

You can set some advanced configuration to tune Unirest-Java:

### Custom HTTP clients

You can explicitly set your own `HttpClient` and `HttpAsyncClient` implementations by using the following methods:

```java
Unirest.setHttpClient(httpClient);
Unirest.setAsyncHttpClient(asyncHttpClient);
```
### Timeouts

You can set custom connection and socket timeout values (in **milliseconds**):

```java
Unirest.setTimeouts(long connectionTimeout, long socketTimeout);
```

By default the connection timeout is `10000`, and the socket timeout is `60000`.

### Default Request Headers

You can set default headers that will be sent on every request:

```java
Unirest.setDefaultHeader("Header1", "Value1");
Unirest.setDefaultHeader("Header2", "Value2");
```

You can clear the default headers anytime with:

```java
Unirest.clearDefaultHeaders();
```

# Exiting an application

If you are using the asynchronous client, a background event loop is started and your Java application won't be able to exit until you manually shutdown all the threads by invoking:

```java
Unirest.shutdown();
```
