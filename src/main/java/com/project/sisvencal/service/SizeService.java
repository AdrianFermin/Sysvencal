package com.project.sisvencal.service;

import com.project.sisvencal.models.Size;
import java.util.List;

public interface SizeService {
    
    public List<Size> listarSize();
    
    public Size encontrarSize(Size size);
}
