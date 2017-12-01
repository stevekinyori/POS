import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { GoodsReturnedToSupplier } from './goods-returned-to-supplier.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class GoodsReturnedToSupplierService {

    private resourceUrl = SERVER_API_URL + 'api/goods-returned-to-suppliers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/goods-returned-to-suppliers';

    constructor(private http: Http) { }

    create(goodsReturnedToSupplier: GoodsReturnedToSupplier): Observable<GoodsReturnedToSupplier> {
        const copy = this.convert(goodsReturnedToSupplier);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(goodsReturnedToSupplier: GoodsReturnedToSupplier): Observable<GoodsReturnedToSupplier> {
        const copy = this.convert(goodsReturnedToSupplier);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<GoodsReturnedToSupplier> {
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
     * Convert a returned JSON object to GoodsReturnedToSupplier.
     */
    private convertItemFromServer(json: any): GoodsReturnedToSupplier {
        const entity: GoodsReturnedToSupplier = Object.assign(new GoodsReturnedToSupplier(), json);
        return entity;
    }

    /**
     * Convert a GoodsReturnedToSupplier to a JSON which can be sent to the server.
     */
    private convert(goodsReturnedToSupplier: GoodsReturnedToSupplier): GoodsReturnedToSupplier {
        const copy: GoodsReturnedToSupplier = Object.assign({}, goodsReturnedToSupplier);
        return copy;
    }
}
