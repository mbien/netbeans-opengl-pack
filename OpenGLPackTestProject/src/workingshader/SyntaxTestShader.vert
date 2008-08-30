#version 120

// GLSL1.3 precission qualifier
precision highp float;
precision mediump int;


varying vec4 vertex[], normal[], texcoords[];
varying vec4 vertex1, normal1, texcoords1;
uniform vec4 lightPosition[];
attribute vec4 position;

float lights[];
float[] lights2;
const int numLights = 2;

float lights[numLights], lights2[numLights];


uniform struct glMaterialParameters {
        vec4 emission;
        vec4 ambient;
        vec4 diffuse;
        vec4 specular;
        float shininess;
} material1, material2;
	
uniform glMaterialParameters material3;

/* Prototype from OpenGL Programming Guide, Fith Edition, pg 652 */
float HornerEvalPolynomial(float coeff[10], float x);

/*function declaration test*/
float fade(in float t);

/*GLSL 1.3*/
in vec3 norm;
centroid in vec2 TexCoord;
flat in vec3 myColor;
//noperspective out float temperature;
invariant centroid in vec4 Color;
//centroid noperspective in vec2 myTexCoord;

// precision
//out mediump vec2 P;

/*
* grammar test.
* this shader does nothing usefull, it is just for grammar tests.
*/
void main() {
   
    // comment
    gl_Position = ftransform();
    
    float y = sin(20.0);
    float x = 10.0*10.0*y;
    float z = (x*y++);
    z++;
    float ext = 0.0;
    z = -((z));
    z = - --z;
    #define F2 0.366025403784
    vec2 v = vec2(0);
    
    (x /*+ y*/) * F2; // strange but valid ;)
    
    float s = (-(x + y)) * -F2;
    s = (x + y) * F2;
    z *= ((2.0));
    z += 2.0 * (x);
    z = (x + y) * z;
    z = (gl_Position.x + gl_Position.y) * gl_Position.z;
    
    // ternary opp
    int t = 5 > 4 ? 25 : 62;
    float t2 = (5+1) > 4 ? fade(1.0) : 62.0;
    t2 = fade(fade(1.0)) > fade(1.0) ? fade(1.0) : fade(1.0);
    t = (5 > 4) ? 25 : 62;
    
    vec3 test = vec3(1.0, 1.0, 1.0);
    test.xyz;
    test.x = 0.0;

    // TODO casts
    int integer = 0;
//    integer = (int)0.0;
    
    // increment/decrement
    gl_Position.x = test.y++;
    --gl_Position.x;
    
    // flow control
    vec2 o1;
    if(x > y) 
      o1 = vec2(1.0, 0.0);
    else if(y > y)
      o1 = vec2(1.0, 1.0);
    else 
      o1 = vec2(0.0, 1.0);

    if(x > y) {
      o1 = vec2(1.0, 0.0);
    }else{
      o1 = vec2(0.0, 1.0);
    }
    
    while(true) 
        o1.x++;
    
    do{
        if(false) {
            while(true) {
            }
        }
    }while(true);

/*
    switch(t) {
        case 1:
            t = 2;
            t = 2;
            break;
        case 2:
        case 3:
            t = 4;
        default:
            t = 0;
    }
*/
    
    for(int i = 0; i < 5; ++i) {
        gl_FrontColor = gl_FrontColorIn[i]; 
        gl_Position = gl_PositionIn[i]; 
        break; 
    }
    
    // arrays
    float a[5] = float[5](3.4, 4.2, 5.0, 5.2, 1.1);
    float b[5] = float[](3.4, 4.2, 5.0, 5.2, 1.1); // same thing
    float c[5];
    c[0] = -1;

    //float[5] d;
    //float[] e = float[](3.4, 4.2, 5.0, 5.2, 1.1);
    
    mat3 m, n, r;
    
    r[0].x = m[0].x * n[0].x + m[1].x * n[0].y + m[2].x * n[0].z;
    r[1].x = m[0].x * n[1].x + m[1].x * n[1].y + m[2].x * n[1].z;
    r[2].x = m[0].x * n[2].x + m[1].x * n[2].y + m[2].x * n[2].z;
    r[0].y = m[0].y * n[0].x + m[1].y * n[0].y + m[2].y * n[0].z;
    r[1].y = m[0].y * n[1].x + m[1].y * n[1].y + m[2].y * n[1].z;
    r[2].y = m[0].y * n[2].x + m[1].y * n[2].y + m[2].y * n[2].z;
    r[0].z = m[0].z * n[0].x + m[1].z * n[0].y + m[2].z * n[0].z;
    r[1].z = m[0].z * n[1].x + m[1].z * n[1].y + m[2].z * n[1].z;
    r[2].z = m[0].z * n[2].x + m[1].z * n[2].y + m[2].z * n[2].z;
    
}

void test(out float a, vec3[5] b, const int c) {
    
}

/*
 * The interpolation function. This could be a 1D texture lookup
 * to get some more speed, but it's not the main part of the algorithm.
 */
float fade(in float t) {
  // return t*t*(3.0-2.0*t); // Old fade, yields discontinuous second derivative
  return (t*t)*t*(t*(t*6.0-15.0)+10.0); // Improved fade, yields C2-continuous noise
}
