varying vec3 Normal;
varying vec4 DiffColor;

void main (void)
{
	vec3 color = vec3(DiffColor.x, DiffColor.y, DiffColor.z);
	float f = dot(vec3(0,0,1),Normal);
	if (abs(f) < 0.6)
		color = vec3(0);
	if (f > 0.95)
	color = vec3(1.0, 0.6, 0.6);

	gl_FragColor = vec4(color + vec3(0.3, 0.3, 0.3), 1);
}
