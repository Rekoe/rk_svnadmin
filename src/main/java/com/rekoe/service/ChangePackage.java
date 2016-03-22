package com.rekoe.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;



import java.util.HashSet;
import java.util.Set;

import org.nutz.lang.Files;
import org.nutz.lang.util.Disks;
import org.nutz.lang.util.FileVisitor;

public class ChangePackage {

	public static void main(String[] args) throws IOException {
		
		final Set<String> suffix = new HashSet<String>();
		suffix.add("csd");
		FileFilter ff = new FileFilter() {
			public boolean accept(File pathname) {
				if (pathname.isDirectory())
					return true;
				return suffix.contains(Files.getSuffixName(pathname));
			}
		};
		FileVisitor fv = new FileVisitor() {
			public void visit(File file) {
				if (file.isDirectory())
					return;
				System.out.println(file);
				String origin = Files.read(file);
				String output = origin.replaceAll("WenQuanYi Micro Hei.ttf", "FZY4JW_0.ttf");
				if (origin.equals(output))
					return;
				Files.write(file, output);
			}
		};
		Disks.visitFile(new File("D:/sources"), fv, ff);
	}
}
