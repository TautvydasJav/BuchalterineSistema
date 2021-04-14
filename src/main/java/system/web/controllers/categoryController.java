package system.web.controllers;

import system.hiberante.CategoryHibernateCtrl;
import system.model.categories.Category;
import system.model.categories.SubCategory;
import system.model.categories.TopCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import system.web.Repositories.CategoryRepository;
import system.web.Repositories.SubCategoryRepository;
import system.web.Repositories.TopCategoryRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


@Controller
@RequestMapping(path = "/category")
public class categoryController {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("AccountingHibernate");
    CategoryHibernateCtrl categoryHibernateCtrl = new CategoryHibernateCtrl(factory);

    @Autowired
    private TopCategoryRepository topCategoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping(path = "/get_top_by_id")
    public @ResponseBody
    TopCategory getTopCatByID(@RequestParam int id) {
        TopCategory topCat = topCategoryRepository.findById(id);
        return topCat;
    }

    @GetMapping(path = "/get_sub_by_id")
    public @ResponseBody
    SubCategory getSubCatByID(@RequestParam int id) {
        SubCategory subCat = subCategoryRepository.findById(id);
        return subCat;
    }

    @GetMapping(path="/allTop")
    public @ResponseBody Iterable<TopCategory> getAllTopCategories(){return topCategoryRepository.findAll(); }

    @GetMapping(path="/allSub")
    public @ResponseBody Iterable<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping(path = "/updateSub")
    public @ResponseBody
    String updateSubCategory(@RequestParam SubCategory category) throws Exception {
        categoryHibernateCtrl.editSubCategory(category);
        return "updated";
    }

    @PostMapping(path = "/updateTop")
    public @ResponseBody
    String updateTopCategory(@RequestParam TopCategory category) throws Exception {
        categoryHibernateCtrl.editTopCategory(category);
        return "updated";
    }

    @DeleteMapping(path="/deleteSub")
    public @ResponseBody String deleteSubCat(@RequestParam Category topCat, Category subCat) throws Exception {
        categoryHibernateCtrl.removeSubCategory(topCat, subCat);
        return "deleted";
    }

}
