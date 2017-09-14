#include <jni.h>



JNIEXPORT jstring JNICALL
Java_com_example_dalek_hello_MainActivity_getMsgFromJni(JNIEnv *env, jobject instance) {
// Put your code here

 return (*env)->NewStringUTF(env, "Hello From JNI");
}


JNIEXPORT jint JNICALL
Java_com_example_dalek_hello_MainActivity_add(JNIEnv *env, jobject object, jint a , jint b)
{
 return a+b;
}
