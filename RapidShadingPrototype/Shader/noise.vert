varying vec4 vertex;
void main()
{
	vertex = gl_Vertex;
	gl_Position = ftransform();
}
