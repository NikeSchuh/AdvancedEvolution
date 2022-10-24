package de.philipp.advancedevolution.util;

import java.util.ArrayList;

public class Sphere
{

    private float[][] coordinates;

    public Sphere(float radius, float thickness, boolean hollow)
    {
        ArrayList<Float[]> dynamicArray = new ArrayList<>();
        for(float x = -radius; x <= radius; x += thickness)
            for(float y = -radius; y <= radius; y += thickness)
                for(float z = -radius, distance = x * x + y * y; z <= radius; z += thickness, distance = x * x + y * y + z * z)
                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1))))
                        dynamicArray.add(new Float[]{x, y, z});
        this.coordinates = new float[dynamicArray.size()][];
        for(int a = 0; a < this.coordinates.length; a++)
        {
            this.coordinates[a] = new float[3];
            for(int b = 0; b < 3; b++)
                this.coordinates[a][b] = dynamicArray.get(a)[b];
        }
    }
}
