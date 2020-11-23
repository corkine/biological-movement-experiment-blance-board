clc;clear;
load('allsub_E1_Scr2.mat');
Xdata=x;
D=[D(1,:);D(3:8,:);D(10:end,:)];% delet sub1 and sub9, they just participant in scr condition
ft = fittype('1/(1+exp(-a*(x-c)))');
for i=1:length(D(:,1))
 goodnessall{i}.rsquare=0;   
while goodnessall{i}.rsquare<0.9
    Ydata=D(i,1:7);
 [fitmodel,goodnessfit]= fit(Xdata',Ydata',ft);
fitmodelA(i)=fitmodel.a;
fitmodelC(i)=fitmodel.c;
goodnessall_Rsquare(i)=goodnessfit.rsquare;
 goodnessall{i}=goodnessfit;
inversef = @(x) -log(1/x-1)/fitmodel.a + fitmodel.c;
PSE(i) = inversef(0.5);
DL(i) = inversef(0.75) - inversef(0.25);
end
end
PSE=PSE';
DL=DL';
goodnessall_Rsquare=goodnessall_Rsquare';
  ExpAllmean=sprintf('allsub_E1_Scr_PSE_CFT2_9');
save(ExpAllmean,'goodnessall_Rsquare','DL','PSE','ft','D','x','goodnessall','fitmodelA','fitmodelC');
% 'fitmodelall','goodnessall'