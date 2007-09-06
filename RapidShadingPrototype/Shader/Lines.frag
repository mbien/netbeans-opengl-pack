varying vec4 Vertex;
varying float V;
void main()
{
	float Frequency = 10.0;
	float value = abs(fract(Vertex.x * Frequency) * 2.0 - 1.0);
	float sFactor = length(vec2(dFdx(V), dFdy(V)));
	sFactor = sFactor * Frequency * 2.0;
	value = smoothstep(0.5 - sFactor, 0.5 + sFactor, value);
	gl_FragColor = vec4(value, value, value, 1.0);
}