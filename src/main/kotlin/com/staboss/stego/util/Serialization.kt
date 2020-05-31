package com.staboss.stego.util

import java.io.*

object Serialization {

    /**
     * Serialize the given object and save it to given filepath
     *
     * @param any the given object
     * @param fileName filepath
     */
    fun serialize(any: Any, fileName: String) {
        val file = File(fileName)
        serialize(any, file)
    }

    /**
     * Serialize the given object and save it to given file
     *
     * @param any the given object
     * @param file file
     */
    fun serialize(any: Any, file: File) {
        FileOutputStream(file).use { fos ->
            BufferedOutputStream(fos).use { bos ->
                ObjectOutputStream(bos).use { oos ->
                    oos.writeObject(any)
                }
            }
        }
    }

    /**
     * Deserialize to Object from given filepath
     *
     * @param fileName filepath
     * @return object from given file
     */
    fun deserialize(fileName: String): Any {
        val file = File(fileName)
        return deserialize(file)
    }

    /**
     * Deserialize to object from given file
     *
     * @param file file
     * @return object from given file
     */
    fun deserialize(file: File): Any {
        FileInputStream(file).use { fis ->
            BufferedInputStream(fis).use { bis ->
                ObjectInputStream(bis).use { ois ->
                    return ois.readObject()
                }
            }
        }
    }
}
