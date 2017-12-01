import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { SupplierProducts } from './supplier-products.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SupplierProductsService {

    private resourceUrl = SERVER_API_URL + 'api/supplier-products';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/supplier-products';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(supplierProducts: SupplierProducts): Observable<SupplierProducts> {
        const copy = this.convert(supplierProducts);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(supplierProducts: SupplierProducts): Observable<SupplierProducts> {
        const copy = this.convert(supplierProducts);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<SupplierProducts> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to SupplierProducts.
     */
    private convertItemFromServer(json: any): SupplierProducts {
        const entity: SupplierProducts = Object.assign(new SupplierProducts(), json);
        entity.firstItemDeliveryDate = this.dateUtils
            .convertLocalDateFromServer(json.firstItemDeliveryDate);
        return entity;
    }

    /**
     * Convert a SupplierProducts to a JSON which can be sent to the server.
     */
    private convert(supplierProducts: SupplierProducts): SupplierProducts {
        const copy: SupplierProducts = Object.assign({}, supplierProducts);
        copy.firstItemDeliveryDate = this.dateUtils
            .convertLocalDateToServer(supplierProducts.firstItemDeliveryDate);
        return copy;
    }
}
