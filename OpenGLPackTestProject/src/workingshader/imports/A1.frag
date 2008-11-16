//import A2.frag
//import A3.frag
//import A4.frag


float a1() {
    return a4();
}

/*
* test A: tests double import prevention
*/
void main() {
    // Set the fragment color for example to gray, alpha 1.0
    gl_FragColor = vec4(a1(), a2(), a3(), a4());
}
