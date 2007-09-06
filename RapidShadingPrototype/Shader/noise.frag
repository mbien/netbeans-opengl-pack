uniform sampler2D noiseMap;
uniform float time;
varying vec4 vertex;

float random(float seed) {
	 return fract(seed * 125.52476 + 0.312345);
}
float random(float x, float y, float z, sampler2D sampler) {
	vec3 position = fract(vec3(x, y, z) / 56.0);
	float index1 = (x * 6.6) + (y * 7.91) + (z * 8.21) * 0.001953125;
	float index2 = (y * 6.64) + (z * 7.851) + (x * 7.451) * 0.01953125;
	vec2 coord = vec2(index1, index2) + time;
	vec4 color = texture2D(sampler, coord);
	return (-1.0 + (color.x + color.y + color.z + color.w) * 0.5);
}
vec3 scurve(vec3 v) {
	return v*v*(3.0-2.0*v);
}
float noise(vec3 v, sampler2D g) {
	vec3 lp = floor(v);
	vec3 frac1 = scurve(fract(v));
	vec4 v1;
	
	v1.x = random(lp.x, lp.y, lp.z, g);
	v1.y = random(lp.x + 1.0, lp.y, lp.z, g);
	v1.z = random(lp.x, lp.y + 1.0, lp.z, g);
	v1.w = random(lp.x + 1.0, lp.y + 1.0, lp.z, g);
	
	vec2 i1 = mix(v1.xz, v1.yw, frac1.x);
	float a = mix(i1.x, i1.y, frac1.y);
	
	v1.x = random(lp.x, lp.y, lp.z + 1.0, g);
	v1.y = random(lp.x + 1.0, lp.y, lp.z + 1.0, g);
	v1.z = random(lp.x, lp.y + 1.0, lp.z + 1.0, g);
	v1.w = random(lp.x + 1.0, lp.y + 1.0, lp.z + 1.0, g);
	
	i1 = mix(v1.xz, v1.yw, frac1.x);
	float b = mix(i1.x, i1.y, frac1.y);
	
	return mix(a, b, frac1.z);
	
}
void main()
{
	vec3 coord = vertex.xyz;
	float value = noise(coord, noiseMap)+
				  noise(coord*2.0, noiseMap)*0.5+
				  noise(coord*4.0, noiseMap)*0.25+
				  noise(coord*8.0, noiseMap)*0.125+
				  noise(coord*16.0, noiseMap)*0.0625;/*+
				  noise(coord*32.0, noiseMap)*0.03125*/
	value = 1.0 - (abs(value) * 1.5);
        //value = 0.5 * (sin(value) + 1.0);
        //value = 0.5 * value + 0.5;
	gl_FragColor = vec4(value, value, value, 1.0);
}
