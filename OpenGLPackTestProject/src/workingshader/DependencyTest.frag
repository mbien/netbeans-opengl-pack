//concat Dependency.frag
/**
 * Created on 31. Dez 2006, 22:35
 *
 * @author Michael Bien
 *
 * test shader
 */
uniform sampler1D colorTexture;
uniform float radius;

varying vec3 texCoord3D;
varying vec4 color;

void main( void ) {

  float k = inversesqrt(    texCoord3D.x*texCoord3D.x
                          + texCoord3D.y*texCoord3D.y
                          + texCoord3D.z*texCoord3D.z   ) * radius;

  float n = 0;

  float amplitude = 1500.0; 
  float frequency = 0.00008;

  for(int i = 0; i < 7; i++) {
    n += mynoise(vec3(texCoord3D * k * frequency)) * amplitude;
    amplitude *= 0.5;
    frequency *= 2.0;
  }

  n = 0.5 + 0.5 * n / 1500.0;

  gl_FragColor = texture1D(colorTexture, n);

}