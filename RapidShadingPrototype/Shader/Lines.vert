varying vec4 Vertex;
varying float V;
void main()
{
	gl_Position = ftransform();
	Vertex = gl_Vertex;
	V = gl_Vertex.x;
} 