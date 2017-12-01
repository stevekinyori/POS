/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SubCategoriesDetailComponent } from '../../../../../../main/webapp/app/entities/sub-categories/sub-categories-detail.component';
import { SubCategoriesService } from '../../../../../../main/webapp/app/entities/sub-categories/sub-categories.service';
import { SubCategories } from '../../../../../../main/webapp/app/entities/sub-categories/sub-categories.model';

describe('Component Tests', () => {

    describe('SubCategories Management Detail Component', () => {
        let comp: SubCategoriesDetailComponent;
        let fixture: ComponentFixture<SubCategoriesDetailComponent>;
        let service: SubCategoriesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [SubCategoriesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SubCategoriesService,
                    JhiEventManager
                ]
            }).overrideTemplate(SubCategoriesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SubCategoriesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubCategoriesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SubCategories(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.subCategories).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
