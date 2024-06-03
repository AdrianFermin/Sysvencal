package com.project.sisvencal.service;

import com.project.sisvencal.datos.SizeDao;
import com.project.sisvencal.models.Size;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SizeServiceImpl implements SizeService{
    
    @Autowired
    private SizeDao sizeDao;

    @Override
    public List<Size> listarSize() {
        return (List<Size>) sizeDao.findAll();
    }

    @Override
    public Size encontrarSize(Size size) {
        return sizeDao.findById(size.getIdSize()).orElse(null);
    }
}
