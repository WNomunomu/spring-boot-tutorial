package com.example

@RestController
@SpringBootApplication
class MyApplication {

  @RequestMapping("/")
  fun home() = "Hello World!"

}

fun main(args: Array<String>) {
  runApplication<MyApplication>(*args)
}
