#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_seung_hellotf_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++ NDK r12b";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jint JNICALL Java_com_example_seung_hellotf_MainActivity_add
        (JNIEnv *env, jobject obj, jint a, jint b)
{
    return a+b;

}
