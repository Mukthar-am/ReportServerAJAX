
my $directory = "/Users/muktharahmed/Data2/git/QA/muks-org-psotest/test-automation/psowebapp/src/main/webapp/images";


opendir (DIR, $directory) or die "Cannot open the dir: $!";
while (my $file = readdir(DIR)) {

    next if ($file =~ m/^\./);
	if (-f $file) { 
		my $cmd = "mv ".$file." banner_".$file;
		print "+ $file\n";
		system($cmd);
	}
    
}
