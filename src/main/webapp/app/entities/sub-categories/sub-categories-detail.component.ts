import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { SubCategories } from './sub-categories.model';
import { SubCategoriesService } from './sub-categories.service';

@Component({
    selector: 'jhi-sub-categories-detail',
    templateUrl: './sub-categories-detail.component.html'
})
export class SubCategoriesDetailComponent implements OnInit, OnDestroy {

    subCategories: SubCategories;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private subCategoriesService: SubCategoriesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSubCategories();
    }

    load(id) {
        this.subCategoriesService.find(id).subscribe((subCategories) => {
            this.subCategories = subCategories;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSubCategories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'subCategoriesListModification',
            (response) => this.load(this.subCategories.id)
        );
    }
}
