varying vec4 Vertex;
void main() {
  vec4 Position;
  Position = ftransform();
  gl_Position = Position;
  Vertex = gl_Vertex;
}