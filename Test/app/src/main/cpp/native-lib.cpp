#include <jni.h>
#include <string>

extern "C"
{
JNIEXPORT jstring JNICALL
Java_com_example_administrator_test_MainActivity_getServerPublicKey(JNIEnv *env,
                                                                    jobject /* this */) {
    std::string publicKeyServer = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7jcsvpPDQo2CyC8yCru2/Rc+0gPz5+H8bD52SeGtIvKy0SeQTniAoSZ7eqLNbSHndIh8W+++DsKeInDwx4TqYtnRHNqyuN0jFYcX4O50o1c5a3qxKSlmsg6rJ42ZA1x1luIsAFgN0KH5Bn8w0STu1/Y8yLDLibjTS1LgnlOlbZQIDAQAB";

    return env->NewStringUTF(publicKeyServer.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_example_administrator_test_MainActivity_getLocalPrivateKey(JNIEnv *env,
                                                                    jobject /* this */) {
    std::string privateKeyLocal = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM9noSv1jdvVgqGZTagmEoxRv4pYu1B0F4gUSwjlINMzpiyN4CZIdZF9kYG9aomuTmrOPvAMa8lSYykiUVFn8qrGHAI4q+CKjgFdFuy0bgpvtm6/lJAl0s+U55DguxB9Tg9HDkMI9jlUsVPw2K1MWegPm3mOCuz6nLQAHpAxyDgDAgMBAAECgYAEXgZCyrTmACT0CipcdBDH66DA5QVsuEUWlHbQBSMfz2KcZZBmvffbzG0X8kkgBQ+ZqCYjH9/VKWr1HsffkvTFIhpuquvmnjSvTcYHQsRCwSFx1xv8fTuvh4NJSmnePm83jo2ysRKXrYkL8oE7u5WS5+NYirK1tb33cUmTCLaD4QJBAOh/lhruBJlXwX5VOO+Ed10n5eZeMjSG2aaRDQJ1BYqltFpv7ENDBK9CYyuh9fwhfiQBmW/eVAHZIqbyajR8f3cCQQDkXrDfrT+5sGd70dXBSn+FiMoLVN1YaAQtoU+cY5oadMkofXVsaxbw/GScLuF1FFWdRTRUey1LMG0glvDhjabVAkEAiccZ9+l3FjwX8Tqlm+LfLXg4WDUYAWKsVnX8xKj+WLRr1XAgDZgIjaeEx1WaRQjADKxZ9h5FoDbBvzdG0sW4bwJALrNWhEdPks+KOf3tMnvjnBEfEDYolZ2fzR0BxL9xkhtzz33od2Vm6Z0TjgTgQisIXwL58pTF7GxYxKNZWya2iQJBAN0APm0M3+OdQFh3uBq5M8yxNjjFneI1pvbEUMdeerNVtEz6VV6DlLO0or9s5v7AF7OCUa/Qex3uERZj6teaSEg=";
    return env->NewStringUTF(privateKeyLocal.c_str());
}
}
