# Hiding data in images using steganography techniques
The Kotlin program, based on steganography methods:

- Kutter-Jordan-Bossen algorithm
- Cox algorithm
- LSB algorithm

## Requirements
- [JAVA 8+](https://www.java.com/en/download/)
- [GRADLE](https://docs.gradle.org/current/userguide/installation.html#installing_with_a_package_manager)

## Build project
    ➜  stego-advanced-hiding: gradle build
    ➜  ...
    ➜  stego-advanced-hiding: gradle jar

## Usage 

    usage: java -jar stego-advanced-hiding.jar -e|-d -a METHOD -s FILE [-r FILE] [-f FILE] -k FILE

```
optional arguments:
  -d           : enable embedding mode
  -e           : enable extracting mode
  -s FILE      : source img file
  -r FILE      : result img file
  -k FILE      : secret key file
  -f FILE      : secret msg file
  -m METHOD    : steganographic method [KUTTER, COX, LSB]
```

## Examples
  #### Kutter-Jordan-Bossen
  - Embedding
    ```
    ➜  java -jar stego-advanced-hiding.jar -e -m KDB -s src.bmp -r res_kdb.bmp -f text.txt -k key.txt
    ```
  - Extracting
    ```
    ➜  java -jar stego-advanced-hiding.jar -d -m KDB -s res_kdb.bmp -k key.txt
    ```
  #### Cox
  - Embedding
    ```
    ➜  java -jar stego-advanced-hiding.jar -e -m COX -s src.bmp -r res_cox.bmp -t text.txt -k alpha_value.txt
    ```
  - Extracting
    ```
    ➜  java -jar stego-advanced-hiding.jar -d -m COX -s res_cox.bmp -k src.bmp
    ```

## License & copyright
Licensed under the [MIT-License](LICENSE.md).