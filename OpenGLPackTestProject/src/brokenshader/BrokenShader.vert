

vec4 transformA() {
    return ftransform();
}
void main() { 
	// Vertex transformation 
	gl_Position = transformB(); 
}

vec4 transformB() {
    return ftransform();
}
