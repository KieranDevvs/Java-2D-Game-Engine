package com.base.RenderingEngine.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.base.MainEngine.Utils;

public abstract class Mesh
{
	protected int vao;
	protected int vbo;
	protected int ibo;
	protected int size;
	
	protected void init()
	{
		this.vao = glGenVertexArrays();
		glBindVertexArray(this.vao);

		this.vbo = glGenBuffers();
		this.ibo = glGenBuffers();
		initVertices();
	}
	
	protected abstract void initVertices();
	/** Recomended implementation:
	 * 
	 *	A vertex array with vertex position data and an int array with the draw pattern
	 * 	this.bufferVertices(Vertex[]..., int[]...);
	 * 
	 */
	
	public void destroy()
	{
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDeleteBuffers(this.vbo);
		glDeleteBuffers(this.ibo);
		glBindVertexArray(0);
		glDeleteVertexArrays(this.vao);
	}
	
	public void bindVertexArray()
	{
		glBindVertexArray(this.vao);
	}

	public void draw()
	{
		this.bindVertexArray();

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, this.size, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);

		this.unbindVertexArray();
	}

	protected void bufferVertices(Vertex[] vertices, int[] indices)
	{
		glBindVertexArray(this.vao);

		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glBufferData(GL_ARRAY_BUFFER, Utils.bufferVertices(vertices), GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 20, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 20, 12);

		this.size = indices.length;

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.createFlippedBuffer(indices), GL_STATIC_DRAW);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void unbindVertexArray()
	{
		glBindVertexArray(0);
	}
	
	protected static int[] genIndices(int length)
	{
		int[] inds = new int[length];
		for(int i = 0; i < inds.length; i++)
		{
			inds[i] = i;
		}
		return inds;
	}
	
	@Override
	protected void finalize()
	{
		this.destroy();
	}
}