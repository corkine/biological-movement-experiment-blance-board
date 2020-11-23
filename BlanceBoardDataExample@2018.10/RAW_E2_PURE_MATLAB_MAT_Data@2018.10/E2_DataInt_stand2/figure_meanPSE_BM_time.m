clc;clear;
load('allsub_E1_Int.mat');

D=[D(1:5,1:7);D(8:12,1:7)]; % D4 and D6 were deleted
   Xdata=x;
    Ydata=mean(D);
ft = fittype('1/(1+exp(-a*(x-c)))');
 [fitmodel,goodnessfit]= fit(Xdata',Ydata',ft)
inversef = @(x) -log(1/x-1)/fitmodel.a + fitmodel.c;
PSE = inversef(0.5)
DLlow = inversef(0.25)
Dlup = inversef(0.75)
DL = inversef(0.75) - inversef(0.25);

H = plot(fitmodel,Xdata',Ydata');
set(H,'LineWidth',1.5);
 axis([-700 700 0 1]);
% annotation('arrow',[55.5228,55.5228],[0.2,0]) 
% annotation('doublearrow',[-250.0913,139.039],[0.2,0.2])  
 
line([-700,700],[0.5,0.5],'linestyle','-','Color','k');  
line([-55.5228,-55.5228],[0,0.5],'linestyle','-','Color','k');  
line([0,0],[0,0.5],'linestyle','-','Color','k');  



line([-700,-250.0913],[0.25,0.25],'linestyle','--','Color','b');
line([-250.0913,-250.0913],[0,0.25],'linestyle','--','Color','b');  

line([-700,139.039],[0.75,0.75],'linestyle','--','Color','b');  
line([139.039,139.039],[0,0.75],'linestyle','--','Color','b');  



text(-98.5228,-0.02,'PSE','FontSize',10,'FontWeight','bold','Fontname', 'Times New Roman');

gcay=ylabel('Proportion of longer responses to upright signals');
set(gcay,'FontSize',12,'FontWeight','bold','Fontname', 'Times New Roman');


  gcax=xlabel('Duration deviation (upr vs. inv, ms)');
set(gcax,'FontSize',12,'FontWeight','bold','Fontname', 'Times New Roman');

 
 lh = legend(H,'','Location','northeast','Orientation','vertical');%2表示图例位置
set(lh,'FontSize',9,'FontWeight','normal','Fontname', 'Times New Roman');
set(lh, 'Box', 'off')
 lh = legend(H,'','Location','northeast','Orientation','vertical');%2表示图例位置
set(lh,'FontSize',9,'FontWeight','normal','Fontname', 'Times New Roman');
set(lh, 'Box', 'off')

 box off;